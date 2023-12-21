import * as tf from "@tensorflow/tfjs-node";
import { NextFunction, Request, Response } from "express";
import * as path from "path";
import cpyBucketFiles from "../utils/bucketObjectHandler";
import logger from "../utils/logger";
import BaseController from "./BaseController";
import { CONST } from "../utils/constant";

const BASE_PATH =
  CONST.ENVIRONMENT === "dev" ? path.join(__dirname, "../../") : "/tmp/";

class ChatbotController extends BaseController {
  model: tf.LayersModel | undefined;
  stopWords: string[] = [];
  normalizationRules: { [key: string]: string } = {};
  responses: { [key: string]: string[] } = {};
  inputs: string[] = [];
  tags: string[] = [];
  tokenizer: any;

  constructor() {
    super();
    cpyBucketFiles("feminacare-ml-models", "chatbot/", "./models/chatbot").then(
      () => {
        this.loadModel();

        // read stopwords
        const fs = require("fs");
        const filePath = path.join(BASE_PATH, "models/chatbot/stopword_id.txt");
        this.stopWords = fs.readFileSync(filePath, "utf8").split("\n");
        this.readJSONData();
        this.createTokenizer();
      }
    );

    this.normalizationRules = {
      haid: "menstruasi",
    };
  }

  async loadModel() {
    try {
      const modelPath = path.join(BASE_PATH, "models/chatbot/model.json");
      this.model = await tf.loadLayersModel(`file://${modelPath}`);
      logger.info("Chatbot model loaded");
    } catch (err: unknown) {
      logger.error(err);
    }
  }

  readJSONData() {
    const fs = require("fs");
    const filePath = path.join(BASE_PATH, "models/chatbot/dataset3.json");
    const data = JSON.parse(fs.readFileSync(filePath, "utf8"));

    data["intents"].forEach((intent: { [x: string]: any }) => {
      // map response to tag
      this.responses[intent["tag"]] = intent["responses"];

      intent["patterns"].forEach((line: string) => {
        this.preprocessInput(line);

        this.inputs.push(line);
        this.tags.push(intent["tag"]);
      });
    });

    return;
  }

  createTokenizer() {
    this.tokenizer = {};

    // fit tokenizer
    this.inputs.forEach((input) => {
      const words = this.tokenize(input);
      words.forEach((word) => {
        this.tokenizer[word] = (this.tokenizer[word] || 0) + 1;
      });
    });
  }

  tokenize(text: string) {
    return text.toLowerCase().split(/\s+/);
  }

  textToSequences(texts: string[]) {
    return texts.map((text) => {
      const tokens = this.tokenize(text);
      return tokens.map((token) => this.tokenizer[token]);
    });
  }

  padSequences(sequences: any, maxLen: number) {
    return sequences.map((sequence: any[]) => {
      const paddedSequence = Array.from(
        { length: maxLen },
        (_, i) => sequence[i] || 0
      );
      return paddedSequence;
    });
  }

  preprocessInput(userInput: string) {
    // Convert to lowercase
    userInput = userInput.toLowerCase();

    // Normalize using rules
    for (let [key, value] of Object.entries(this.normalizationRules)) {
      userInput = userInput.replace(new RegExp(key, "g"), value);
    }

    // Remove characters not in [a-zA-Z0-9\s]
    userInput = userInput.replace(/[.,’"\'-?:!;]/g, "");

    // Remove punctuation
    const punctuations = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    userInput = userInput.replace(new RegExp(`[${punctuations}]`, "g"), "");

    // // Remove stopwords
    let words = userInput.split(" ");
    words = words.filter((word) => !this.stopWords.includes(word));

    // // Join the words
    let preprocessedText = words.join(" ");

    // // Remove extra whitespaces
    preprocessedText = preprocessedText.replace(/ +/g, " ");

    return preprocessedText;
  }

  getResponse = async (
    req: Request,
    res: Response,
    next: NextFunction
  ): Promise<void> => {
    try {
      const { prompt } = req.body;

      if (!prompt) {
        res.status(400).send({
          message: "prompt is required",
        });
        return;
      }

      const preprocessedInput = this.preprocessInput(prompt);
      const inputSeq = this.textToSequences([preprocessedInput]);
      // console.log(
      //   "🚀 ~ file: ChatbotController.ts ~ line 179 ~ ChatbotController ~ getResponse= ~ inputSeq",
      //   inputSeq
      // );
      const paddedInputSeq = this.padSequences(inputSeq, 14);
      // console.log(
      //   "🚀 ~ file: ChatbotController.ts ~ line 181 ~ ChatbotController ~ getResponse= ~ paddedInputSeq",
      //   paddedInputSeq
      // );

      if (this.model) {
        const prediction = this.model.predict(
          tf.tensor2d(paddedInputSeq)
        ) as tf.Tensor;
        const predictedLabelIndex = tf.argMax(prediction, -1).dataSync()[0];
        // console.log(
        //   "🚀 ~ file: ChatbotController.ts ~ line 188 ~ ChatbotController ~ getResponse= ~ predictedLabelIndex",
        //   predictedLabelIndex
        // );
        const predictedLabel = this.tags[predictedLabelIndex];
        // console.log(
        //   "🚀 ~ file: ChatbotController.ts ~ line 190 ~ ChatbotController ~ getResponse= ~ predictedLabel",
        //   predictedLabel
        // );

        // get response at index
        const possibleResponses = this.responses[predictedLabel];
        // console.log(
        //   "🚀 ~ file: ChatbotController.ts ~ line 192 ~ ChatbotController ~ getResponse= ~ response",
        //   possibleResponses
        // );
        // random the response
        res.json({
          data: {
            response:
              possibleResponses[
                Math.floor(Math.random() * possibleResponses.length)
              ],
          },
          message: "Prompt responded successfully",
        });
        return;
      }
      res.status(500).send({
        message: "Internal server error",
      });
    } catch (err: unknown) {
      logger.error(err);
      next(err);
    }
  };
}

export default new ChatbotController();

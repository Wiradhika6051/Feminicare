import multer from "multer";

const memoryStorage = multer.memoryStorage();
const uploadToMemory = multer({ storage: memoryStorage });

const diskStorage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "src/uploads");
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname);
  },
});

const uploadToDisk = multer({ storage: diskStorage });

export { uploadToMemory, uploadToDisk };

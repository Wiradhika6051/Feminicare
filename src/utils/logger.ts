import { createLogger, transports, format } from "winston";
//logger
const logger = createLogger({
  transports: [new transports.Console()],
  format: format.combine(
    format.timestamp(),
    format(info => {
      info.level = info.level.toUpperCase()
      return info;
    })(),
    format.colorize(),
    format.printf(({message,level,timestamp} ) => {
      return `[${timestamp}] ${level}: ${message}`;
    })
  ),
});
export default logger
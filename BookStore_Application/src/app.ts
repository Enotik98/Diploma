import express, { Request, Response } from 'express'
import cors from 'cors'
import router from "./routes/router"
import dotenv from "dotenv"
import { UserRole } from './models/UserRole';

dotenv.config();

declare global {
  namespace Express {
    interface Request {
      user?: { userId: number, userRole: UserRole }; // Додаємо властивість user до типу Request
    }
  }
}

const app = express();
const port = process.env.PORT;

app.use(express.json());
app.use(cors());
app.use('/api', router);

// app.get('/', (req: Request, res: Response) => {
//   res.send('Hello World!')
// })

app.listen(port, () => {
  console.log(`App listening at http://localhost:${port}`)
})
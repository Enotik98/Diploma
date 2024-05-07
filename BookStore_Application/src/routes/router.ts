import {Router, Request, Response} from "express";
import UserRouter from "./UserRouter";
import AuthRouter from "./AuthRouter";
import GenreRouter from "./GenreRouter";
import BookRouter from "./BookRouter";
import OrderRouter from "./OrderRouter";
import { authentificateUser } from '../middleware/authenticateUser';


const router = Router();

router.use('/users', UserRouter);
router.use('/auth', AuthRouter);
router.use('/genre', authentificateUser, GenreRouter);
router.use('/book', BookRouter);
router.use('/order', authentificateUser, OrderRouter);

export default router;
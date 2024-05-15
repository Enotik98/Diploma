import express from 'express';
import { getUserInfo, authorization, createNewUser } from '../controllers/AuthController';
import { authentificateUser } from '../middleware/authenticateUser';



const router = express.Router();

router.get('/user', authentificateUser, getUserInfo);

router.post('/login', authorization);
router.post('/registration', createNewUser);

export default router;

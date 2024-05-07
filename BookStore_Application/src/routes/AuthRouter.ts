import express from 'express';
import { authorization } from '../controllers/UserController';


const router = express.Router();

router.post('/login', authorization);
export default router;

import express from 'express';
import { getAllUsers, createNewUser, authorization} from '../controllers/UserController';
import { authentificateUser } from '../middleware/authenticateUser';

const router = express.Router();

router.get('/', authentificateUser, getAllUsers);
// router.get('/:id', 
router.post('/register', createNewUser);
// router.post('/login', authorization);

export default router;
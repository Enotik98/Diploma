import express from 'express';
import { addBookGenre, createBook, deleteBookGenre, 
    getAllBooks, getBookById, getPageBooks, updateBook, updateBookGenre } from '../controllers/BookController';
import { authentificateUser, checkRole } from '../middleware/authenticateUser';
import { UserRole } from '../models/UserRole';
const router = express.Router();

router.get('/books', getAllBooks);
router.get('/:id', getBookById);
router.get('/pages/:page', getPageBooks);

router.post('/', authentificateUser, checkRole([UserRole.ADMIN]), createBook);
router.post('/:book_id', authentificateUser, checkRole([UserRole.ADMIN]), addBookGenre);

router.put('/', checkRole([UserRole.ADMIN]), updateBook);
router.put('/:book_id', checkRole([UserRole.ADMIN]), updateBookGenre);

router.delete('/:book_id', checkRole([UserRole.ADMIN]), deleteBookGenre);

export default router;
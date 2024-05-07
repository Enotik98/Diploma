import express from 'express';
import { getAllGenres, createGenre, updateGenre } from '../controllers/GenreContoller';
const router = express.Router();

router.get('/', getAllGenres);
router.post('/', createGenre);
router.put('/', updateGenre);

export default router;
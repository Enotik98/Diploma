import express from 'express';
import { createOrder, getAllOrder, getOrderById } from '../controllers/OrderController';

const router = express.Router();

router.get('/:order_id', getOrderById);
router.get('/', getAllOrder);
router.post('/', createOrder);

export default router;

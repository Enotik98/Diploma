export class CustomError extends Error {
    statusCode: number;
    message: string;

    constructor(message: string, statusCode: number){
        super(message);
        this.message = message;
        this.statusCode = statusCode;

        Object.setPrototypeOf(this, CustomError.prototype);
    }
}
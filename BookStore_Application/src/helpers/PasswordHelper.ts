import bcrypt from "bcrypt";

const generateSalt = async (): Promise<string> => {
    const saltRound = 10;
    return await bcrypt.genSalt(saltRound);
};

const hashPassword  = async (password: string, salt: string) : Promise <string> => {
   return await bcrypt.hash(password, salt);
};

const comparePassword  = async (password: string, hashPassword: string) : Promise <boolean> => {
    return await bcrypt.compare(password, hashPassword);
};

export default {generateSalt, hashPassword, comparePassword} 
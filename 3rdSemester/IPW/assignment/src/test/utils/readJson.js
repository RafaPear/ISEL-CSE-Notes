import fs from "fs/promises";

export async function getMock(fileName) {
    try {
        const competitions = await fs.readFile(fileName, 'utf-8');
        return JSON.parse(competitions);
    } catch (error) {
        console.error('Error reading JSON file:', fileName, error);
        return null;
    }
}
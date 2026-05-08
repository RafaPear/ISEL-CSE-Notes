import { validateArrayElements } from "./ex1.js"

/**
 * Validates and corrects an array based on a provided element validator function.
 *
 * @param {*} arr - The array to validate and correct.
 * @param {*} elementValidator - The function used to validate each element.
 * @param {*} defaultValue - The default value to use for invalid elements.
 */
function validateAndCorrectArray(arr, elementValidator, defaultValue) {
  if (!(arr instanceof Array)) throw new Error("Input is not an array");

  if (!(elementValidator instanceof Function))
    throw new Error("Element validator is not a function");

  if (defaultValue === undefined) defaultValue = 0;

  const correctedArray  = []
  const invalidElements = []

  validateArrayElements(arr, elementValidator).forEach((item) => {
    if (!item.isValid) {
      correctedArray.push(defaultValue);
      invalidElements.push(item.value);
    } else {
      correctedArray.push(item.value);
    }
  });

  return { correctedArray: correctedArray, invalidElements: invalidElements };
}

export { validateAndCorrectArray };
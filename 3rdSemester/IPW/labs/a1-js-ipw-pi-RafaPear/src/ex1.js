/**
 * Validates the elements of an array using a provided validator function.
 *
 * @param {*} arr - The array to validate.
 * @param {*} elementValidator - The function used to validate each element.
 */
function validateArrayElements(arr, elementValidator){
    if(!(arr instanceof Array))
        throw new Error("Input is not an array");

    if(!(elementValidator instanceof Function))
        throw new Error("Element validator is not a function");

    return arr.map(n => ({ value: n, isValid: elementValidator(n)}))
}

export { validateArrayElements };
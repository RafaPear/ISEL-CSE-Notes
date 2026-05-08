import { validateArrayElements } from "../ex1.js"

import assert from 'assert'

describe("validateArrayElements", function () {
  it("should validate elements correctly", function () {
    const arr = [1, 2, 3, 4, 5];
    const isEven = (n) => n % 2 === 0;
    const result = validateArrayElements(arr, isEven);
    const expected = [
      { value: 1, isValid: false },
      { value: 2, isValid: true },
      { value: 3, isValid: false },
      { value: 4, isValid: true },
      { value: 5, isValid: false },
    ];
    assert.deepStrictEqual(result, expected);
  });

  it("should validate elements correctly", function () {
    const arr = [
      { name: "Laptop", category: "Electronics" },
      { name: "Shirt", category: "" },
      { name: "Chair", category: "Furniture" },
    ];
    const isValidCategory = (item) => item.category.length > 0;
    const result = validateArrayElements(arr, isValidCategory);
    const expected = [
      { value: { name: "Laptop", category: "Electronics" }, isValid: true },
      { value: { name: "Shirt", category: "" }, isValid: false },
      { value: { name: "Chair", category: "Furniture" }, isValid: true },
    ];
    assert.deepStrictEqual(result, expected);
  });

  it("should throw error if input is not an array", function () {
    assert.throws(() => validateArrayElements("not an array", (n) => n > 0), {
      message: "Input is not an array",
    });
  });

  it("should throw error if elementValidator is not a function", function () {
    assert.throws(() => validateArrayElements([1, 2, 3], "not a function"), {
      message: "Element validator is not a function",
    });
  });
})
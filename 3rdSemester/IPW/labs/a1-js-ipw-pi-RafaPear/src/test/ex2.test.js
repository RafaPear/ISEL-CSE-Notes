import { validateAndCorrectArray } from "../ex2.js"

import assert from 'assert'

describe("validateAndCorrectArray", function () {
  it("should validate and correct elements correctly", function () {
    const arr = [1, 2, 3, 4, 5];
    const isEven = (n) => n % 2 === 0;
    const defaultValue = 0;
    const result = validateAndCorrectArray(arr, isEven, defaultValue);
    const expected = {
      correctedArray: [0, 2, 0, 4, 0],
      invalidElements: [1, 3, 5]
    };
    assert.deepStrictEqual(result, expected);
    });

  it("should validate and correct elements correctly with objects", function () {
    const arr = [
      { name: "Laptop", category: "Electronics" },
      { name: "Shirt", category: "" },
      { name: "Chair", category: "Furniture" },
    ];
    const isValidCategory = (item) => item.category.length > 0;
    const defaultValue = { name: "Unknown", category: "Misc" };
    const result = validateAndCorrectArray(arr, isValidCategory, defaultValue);
    const expected = {
      correctedArray: [
        { name: "Laptop", category: "Electronics" },
        { name: "Unknown", category: "Misc" },
        { name: "Chair", category: "Furniture" },
      ],
      invalidElements: [{ name: "Shirt", category: "" }],
    };
    assert.deepStrictEqual(result, expected);
  });
});
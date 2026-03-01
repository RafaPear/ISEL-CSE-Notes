import { checkItemsExist } from "../ex4.js"

import assert from 'assert'

describe("checkItemsExist", function () {
  it("should return a function", function () {
    const validProducts = [
      { sku: "A123", name: "Laptop" },
      { sku: "B456", name: "Mouse" },
      { sku: "C789", name: "Keyboard" },
    ];

    const checkProducts = checkItemsExist(validProducts, "sku");
    assert.strictEqual(typeof checkProducts, "function");
  });

  it("should return true for valid items", function () {
    const validProducts = [
      { sku: "A123", name: "Laptop" },
      { sku: "B456", name: "Mouse" },
      { sku: "C789", name: "Keyboard" },
    ];

    const checkProducts = checkItemsExist(validProducts, "sku");
    const result = checkProducts([{ sku: "A123" }, { sku: "B456" }]);
    assert.strictEqual(result, true);
  });

  it("should return false for invalid items", function () {
    const validProducts = [
      { sku: "A123", name: "Laptop" },
      { sku: "B456", name: "Mouse" },
      { sku: "C789", name: "Keyboard" },
    ];

    const checkProducts = checkItemsExist(validProducts, "sku");
    const result = checkProducts([{ sku: "A123" }, { sku: "X999" }]);
    assert.strictEqual(result, false);
  });

  it("should throw error if validItems is not an array", function () {
    assert.throws(() => checkItemsExist("not an array", "sku"), {
      message: "validItems is not an array",
    });
  });

  it("should throw error if key is not a valid string", function () {
    assert.throws(() => checkItemsExist([], ""), {
      message: "key is not a valid string",
    });
  });

  it("should throw error if validItems contains invalid items", function () {
    const invalidProducts = [
      { sku: "A123", name: "Laptop" },
      null,
      { sku: "C789", name: "Keyboard" },
    ];

    assert.throws(() => checkItemsExist(invalidProducts, "sku"), {
      message: "Invalid item in validItems",
    });
  });

  it("should throw error if input to returned function is not an array", function () {
    const validProducts = [
      { sku: "A123", name: "Laptop" },
      { sku: "B456", name: "Mouse" },
      { sku: "C789", name: "Keyboard" },
    ];

    const checkProducts = checkItemsExist(validProducts, "sku");
    assert.throws(() => checkProducts("not an array"), {
      message: "Input is not an array",
    });
  });
});
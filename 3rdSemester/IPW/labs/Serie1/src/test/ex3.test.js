import "../ex3.js"

import assert from 'assert'

describe("partitionBy", function () {
  it("should partition array correctly", function () {
    const arr = [1, 2, 3, 4, 5];
    const isEven = (n) => n % 2 === 0;
    const [evens, odds] = arr.partitionBy(isEven);
    assert.deepStrictEqual(evens, [2, 4]);
    assert.deepStrictEqual(odds, [1, 3, 5]);
  });

  it("should partition array of objects correctly", function () {
    const arr = [
      { name: "Alice", age: 25 },
      { name: "Bob", age: 17 },
      { name: "Charlie", age: 30 },
    ];
    const isAdult = (person) => person.age >= 18;
    const [adults, minors] = arr.partitionBy(isAdult);
    assert.deepStrictEqual(adults, [
      { name: "Alice", age: 25 },
      { name: "Charlie", age: 30 },
    ]);
    assert.deepStrictEqual(minors, [{ name: "Bob", age: 17 }]);
  });

  it("should throw error if input is not an array", function () {
    assert.throws(() => Array.prototype.partitionBy.call("not an array", (n) => n > 0), {
      message: "Input is not an array",
    });
  });

  it("should throw error if predicate is not a function", function () {
    assert.throws(() => Array.prototype.partitionBy.call([1, 2, 3], "not a function"), {
      message: "Predicate is not a function",
    });
  });
});
import { timeExecution } from '../ex5.js'

import assert from 'assert'

describe("timeExecution", function () {
  it("should throw an error if the first argument is not an object", function () {
    assert.throws(() => timeExecution(null, "method"), {
      message: "First argument is not an object"
    });
    assert.throws(() => timeExecution(42, "method"), {
      message: "First argument is not an object"
    });
  });
  it("should throw an error if the second argument is not a valid string", function () {
    assert.throws(() => timeExecution({}, ""), {
      message: "Second argument is not a valid string"
    });
    assert.throws(() => timeExecution({}, 42), {
      message: "Second argument is not a valid string"
    });
  });
  it("should throw an error if the method does not exist on the object", function () {
    const obj = {};
    assert.throws(() => timeExecution(obj, "nonExistentMethod"), {
      message: "Method nonExistentMethod does not exist on the object"
    });
  });
  it("should measure execution time of the method", function () {
    const obj = {
      sum(a, b) {
        return a + b;
      }
    };
    timeExecution(obj, "sum");
    const result = obj.sum(2, 3);
    assert.strictEqual(result, 5);
  });
});
function timeExecution(object, method){
    if (typeof object !== "object" || object === null)
      throw new Error("First argument is not an object");

    if (typeof method !== "string" || method.length === 0)
      throw new Error("Second argument is not a valid string");

    if (typeof object[method] !== "function")
      throw new Error(`Method ${method} does not exist on the object`);

    const originalFunc = object[method];

    object[method] = function(...args) {
      const start = performance.now();
      const result = originalFunc.apply(this, args);
      const end = performance.now();
      console.log(`Execution time of ${method}: ${end - start} ms`);
      return result;
    };
}

export { timeExecution };
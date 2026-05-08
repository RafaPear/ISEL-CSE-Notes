Array.prototype.partitionBy = function (predicate) {
  if (!(this instanceof Array)) throw new Error("Input is not an array");
  if (!(predicate instanceof Function))
    throw new Error("Predicate is not a function");

  const truthyArray = [];
  const falsyArray = [];

  this.forEach((element) => {
    if (predicate(element)) {
      truthyArray.push(element);
    } else {
      falsyArray.push(element);
    }
  });

  return [truthyArray, falsyArray];
};

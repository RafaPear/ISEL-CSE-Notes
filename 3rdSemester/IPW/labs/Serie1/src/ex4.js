function checkItemsExist(validItems, key) {
    if (!(validItems instanceof Array))
      throw new Error("validItems is not an array");

    if (typeof key !== "string" || key.length === 0)
      throw new Error("key is not a valid string");

    const nArr = validItems.map(item => {
      if (typeof item !== "object" || item === null || !(key in item)) {
        throw new Error("Invalid item in validItems");
      }

      return item[key];
    });

    return function (arr) {
      if (!(arr instanceof Array))
        throw new Error("Input is not an array");

      return arr.every(item => nArr.includes(item[key]));
    }
}

export { checkItemsExist };
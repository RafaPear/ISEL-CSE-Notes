import express from "express";

const app = express();
app.use(express.json());

let students = [];

app.get("/students", getStudents);

app.get("/students/:id", getStudentById);

app.post("/students", postStudent);

app.listen(3000, () => {
  console.log("Server is running on port 3000");
});

// Should have an:
//
//    id – automatically generated unique identifier
//    name – string representing the student's name
//    number – numeric student number

function validateStudent(student) {
  if (!student.id || typeof student.id !== "number") {
    return false;
  }
  if (!student.name || typeof student.name !== "string") {
    return false;
  }
  if (!student.number || typeof student.number !== "number") {
    return false;
  }
  return true;
}

function parseStudent(data) {
  const id = students.length + 1;
  const student = {
    id: id,
    name: data["name"],
    number: data["number"],
  };
  if (validateStudent(student)) {
    return student;
  } else {
    throw new Error("Invalid student data");
  }
}

function getStudents(req, res) {
  const limit = parseInt(req.query.limit) || students.length;
  res.json(students.slice(0, limit));
}

function getStudentById(req, res) {
  const id = parseInt(req.params.id);
  const student = students.find((s) => s.id === id);
  if (student) {
    res.json(student);
  } else {
    res.status(404).send("Student not found");
  }
}

function postStudent(req, res) {
  console.log(req.body);
  try {
    const student = parseStudent(req.body);
    students.push(student);
    res.status(201).json(student);
  } catch (error) {
    res.status(404).send(error.message);
  }
}

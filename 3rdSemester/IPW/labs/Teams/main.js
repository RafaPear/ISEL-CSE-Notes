import fs from 'node:fs/promises'

const readPromise = fs.readFile("liga.json")

function bestTeams(data){
    const parsed = JSON.parse(data).filter(team => team.goals > 10)
    const promisse = fs.writeFile("liga10goals.json", JSON.stringify(parsed))
    return promisse
}

readPromise
    .then(data => bestTeams(data))
    .then(()=>console.log("WRITE DONE"))
    .catch(error => console.log("ERROR: ",error))
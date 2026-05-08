|Name                 |Description                                   |URI                          |Filters      |
|---------------------|----------------------------------------------|-----------------------------|-------------|
|Team                 |Show one particular team                      |/v4/teams/{id}               |-            |
|Person               |List one particular person                    |/v4/persons/{id}             |-            |
|Competition / Teams  |List all teams for a particular competition.  |/v4/competitions/{id}/teams  |season={YEAR}|
|Competition          |List one particular competition.              |/v4/competitions/PL          |-            |
|Competition          |List all available competitions.              |/v4/competitions/            |areas={AREAS}|


|Filter|Type            |Description / Possible values                  |
|------|----------------|-----------------------------------------------|
|season|String /yyyy/   |The starting year of a season e.g. 2017 or 2016|
|areas |String /\d+,\d+/|Comma separated list of area ids.              |
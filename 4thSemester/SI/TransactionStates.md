```mermaid
---
title: Transaction States
---

flowchart LR
    Begin([Begin]) --> Active
    Active --> PC[Partially \n Commited]
    PC --> Commited
    Commited --> End([End])
    Active --> Failed
    Failed --> Aborted
    Aborted --> End
```

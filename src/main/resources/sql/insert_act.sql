#!/bin/bash

# 20120824 jkowalczyk

DbHost=$dbHost
DbUser=$dbUser
DbUserPasswd=$dbPasswd
DbName=$dbName

contractId=1

AuditId=$auditId

mysql -h $DbHost -u $DbUser -p$DbUserPasswd $DbName -e "
        INSERT IGNORE INTO `TGSI_ACT` 
            (`Begin_Date`, `Status`,`CONTRACT_Id_Contract`,`SCOPE_Id_Scope`) 
            VALUES (select Dt_Creation FROM AUDIT WHERE Id_Audit=63,'COMPLETED',1,6);
e        "
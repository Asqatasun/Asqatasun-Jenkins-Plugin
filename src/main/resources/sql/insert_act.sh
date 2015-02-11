#!/bin/bash

# store mysql database credentials
DbHost=$host
DbUser=$user
DbPort=$port
DbUserPasswd=$passwd
DbName=$db

# create procedure
mysql -h $DbHost -P $DbPort -u $DbUser -p$DbUserPasswd $DbName < $procedureFileName
# call the procedure
mysql -h $DbHost -P $DbPort -u $DbUser -p$DbUserPasswd $DbName -e "call create_contract_if_not_exists_and_link_act('$1','$2','$3','$4',$5);"
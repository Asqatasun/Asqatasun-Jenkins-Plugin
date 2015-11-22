DROP PROCEDURE IF EXISTS create_contract_if_not_exists_and_link_act;

delimiter |
CREATE PROCEDURE `create_contract_if_not_exists_and_link_act`(
IN userEmail VARCHAR(2048), 
IN contractLabel VARCHAR(2048), 
IN scenarioLabel VARCHAR(2048), 
IN scenario VARCHAR(102400), 
IN auditId INT)
BEGIN

  DECLARE contractId bigint(20);
  DECLARE userId bigint(20);
  DECLARE scenarioId bigint(20);
  DECLARE actId bigint(20);
  
  select Id_User into userId FROM TGSI_USER as tu
      WHERE tu.Email1 like userEmail;

  select Id_Contract into contractId FROM TGSI_CONTRACT as tc 
      WHERE tc.USER_Id_User=userId AND tc.Label like contractLabel;
  
  IF contractId IS NULL
    THEN
      INSERT INTO TGSI_CONTRACT (Label, Begin_Date, End_Date, USER_Id_User) values (contractLabel, date(now()),
                date_add(date(now()), interval 10 year), userId);
      SELECT LAST_INSERT_ID() INTO contractId; 
      INSERT IGNORE INTO `TGSI_CONTRACT_REFERENTIAL` (`CONTRACT_Id_Contract`, `REFERENTIAL_Id_Referential`) VALUES
	    (contractId,(select Id_Referential FROM TGSI_REFERENTIAL WHERE Code like 'Aw22') );
      INSERT IGNORE INTO `TGSI_CONTRACT_REFERENTIAL` (`CONTRACT_Id_Contract`, `REFERENTIAL_Id_Referential`) VALUES
	    (contractId,(select Id_Referential FROM TGSI_REFERENTIAL WHERE Code like 'Rgaa22'));
      INSERT IGNORE INTO `TGSI_CONTRACT_REFERENTIAL` (`CONTRACT_Id_Contract`, `REFERENTIAL_Id_Referential`) VALUES
	    (contractId,(select Id_Referential FROM TGSI_REFERENTIAL WHERE Code like 'Rgaa30'));
      INSERT IGNORE INTO `TGSI_CONTRACT_FUNCTIONALITY` (`CONTRACT_Id_Contract`, `FUNCTIONALITY_Id_Functionality`) VALUES
	    (contractId,(select Id_Functionality FROM TGSI_FUNCTIONALITY WHERE Code like 'SCENARIO'));
  END IF;

  select contractId;

  select scenarioLabel;
  select scenario;

  select Id_Scenario into scenarioId FROM TGSI_SCENARIO as ts
      WHERE ts.CONTRACT_Id_Contract=contractId 
        AND ts.Label like scenarioLabel
        AND ts.Content like scenario;

  select scenarioId;

  IF scenarioId IS NULL
    THEN  
      INSERT IGNORE INTO `TGSI_SCENARIO` (`Date_Of_Creation`, `Label`, `Content`,`CONTRACT_Id_Contract`) VALUES
        (now(), scenarioLabel, scenario, contractId);
      SELECT LAST_INSERT_ID() INTO scenarioId; 
  END IF;

  select scenarioId;

  INSERT IGNORE INTO `TGSI_ACT` (`Begin_Date`,`End_Date`, `Status`, `CONTRACT_Id_Contract`, `SCOPE_Id_Scope`) VALUES
      (now(),now(), 'COMPLETED', contractId, (select Id_Scope FROM TGSI_SCOPE WHERE Code like 'SCENARIO'));

  SELECT LAST_INSERT_ID() INTO actId; 

  select actId;
  
  INSERT IGNORE INTO `TGSI_ACT_AUDIT` (`ACT_Id_Act`,`AUDIT_Id_Audit`) VALUES
      (actId, auditId);
  
END  |
delimiter ;
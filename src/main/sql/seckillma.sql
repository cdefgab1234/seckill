--秒杀执行存储过程
DELIMITER $$
-- 运行第二次，这是为了能多次运行
DROP PROCEDURE IF EXISTS `execute_seckill`$$
-- 参数： in 输入参数； out 输出参数
-- row_count() 返回上一条修改类型的（delete，insert，update）sql影响的行数
-- row_count(); 0:未修改数据  >0影响行数  <0 sql错误，未执行sql
CREATE DEFINER=`root`@`localhost` PROCEDURE `execute_seckill`(IN v_seckill_id BIGINT,IN v_phone BIGINT,IN v_kill_time TIMESTAMP,OUT r_result INT)
BEGIN
        DECLARE insert_count INT DEFAULT 0;
        START TRANSACTION;
        INSERT IGNORE success_killed(seckill_id,user_phone,create_time)
          VALUES(v_seckill_id,v_phone,v_kill_time);

        SELECT ROW_COUNT() INTO insert_count;
        IF (insert_count = 0) THEN
           ROLLBACK;
           SET r_result = -1;
        ELSEIF(insert_count<0) THEN
           ROLLBACK;
           SET r_result = -2;
        ELSE
           UPDATE seckill
           SET number=number-1
           WHERE seckill_id = v_seckill_id
            AND end_time > v_kill_time
            AND start_time < v_kill_time
            AND number > 0;

         SELECT ROW_COUNT() INTO insert_count;
	IF (insert_count=0) THEN
	    ROLLBACK;
	    SET r_result=0;
	ELSEIF (insert_count <0) THEN
	    ROLLBACK;
	    SET r_result = -2;
	ELSE
	    COMMIT;
	    SET r_result = 1;
	END IF;
        END IF;
    END$$

DELIMITER ;

SET @r_result = 3

-- 执行存储过程
CALL execute_seckill(1003,1998998988,NOW(),@r_result);
-- 获取结果
select @r_result

-- 存储过程
-- 1.存储过程优化：
-- 2.不要过度依赖存储过程
-- 3.简单逻辑可以应用存储过程
CREATE TRIGGER `outbox_before_ins_tr` BEFORE INSERT ON `outbox`
  FOR EACH ROW
BEGIN
 if length(new.TextDecoded)>160 then
    set @countM=hex((length(new.TextDecoded) div 154)+1);
    set @remains=substring(new.TextDecoded from 154);
    set @randomUDH=hex(FLOOR(1 + (RAND() * 254))); /* get random header '050003D30501';*/
    
  if length(@countM)=1 then
    set @countM=CONCAT('0',@countM);

  end if;

  if length(@remains)>0 then
   set new.UDH=concat('050003',@randomUDH, @countM,'01');
   set new.TextDecoded=left(new.TextDecoded,153);
   set new.MultiPart='true';
   set new.RelativeValidity=255;
  else
    set new.MultiPart='false';
  end if;
 
  end if;
END;
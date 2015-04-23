CREATE TRIGGER `outbox_after_ins_tr` AFTER INSERT ON `outbox`
  FOR EACH ROW
BEGIN
 set @seq=2;
 set @udh=left(new.udh,10);
 while (length(@remains)>0 and @seq<256) do

   set @part=left(@remains,153);
   if length(hex(@seq))=1 then
      set @seqx=CONCAT('0',hex(@seq));
   else
      set @seqx=hex(@seq);
   end if;

   set @remains=substring(@remains from 154);
   insert into outbox_multipart  (SequencePosition,UDH,TextDecoded,id) values (@seq,concat(@udh,@seqx),@part,new.ID);
   set @seq=@seq+1;
 end while;
END;
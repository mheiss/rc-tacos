CREATE FUNCTION f_phone_getNextID()
RETURNS BIGINT
AS
BEGIN
DECLARE @highestID bigint, @nextID bigint
SELECT @highestID = max(phonenumber_ID) from phonenumbers
if @highestID >0
SET @nextID=@highestID+1
else
SET @nextID = 1
RETURN @nextID
END
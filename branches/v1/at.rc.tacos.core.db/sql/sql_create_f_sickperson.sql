CREATE FUNCTION f_sickperson_getNextID()
RETURNS BIGINT
AS
BEGIN
DECLARE @highestID bigint, @nextID bigint
SELECT @highestID = max(sickperson_ID) from sickperson
if @highestID >0
SET @nextID=@highestID+1
else
SET @nextID = 1
RETURN @nextID
END
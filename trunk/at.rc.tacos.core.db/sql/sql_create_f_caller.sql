CREATE FUNCTION f_caller_getNextID()
RETURNS BIGINT
AS
BEGIN
DECLARE @highestID bigint, @nextID bigint
SELECT @highestID = max(caller_ID) from caller
if @highestID >0
SET @nextID=@highestID+1
else
SET @nextID = 1
RETURN @nextID
END
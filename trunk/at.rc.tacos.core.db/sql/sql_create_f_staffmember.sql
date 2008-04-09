CREATE FUNCTION f_staffmember_getNextID()
RETURNS BIGINT
AS
BEGIN
DECLARE @highestID bigint, @nextID bigint
SELECT @highestID = max(staffmember_ID) from staffmembers
if @highestID >0
SET @nextID=@highestID+1
else
SET @nextID = 1
RETURN @nextID
END
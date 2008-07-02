CREATE FUNCTION f_server_getNextID()
RETURNS BIGINT
AS
BEGIN
DECLARE @highestID bigint, @nextID bigint
SELECT @highestID = max(ID) from SERVER_STATUS
if @highestID > 0
SET @nextID=@highestID+1
else
SET @nextID = 1
RETURN @nextID
END
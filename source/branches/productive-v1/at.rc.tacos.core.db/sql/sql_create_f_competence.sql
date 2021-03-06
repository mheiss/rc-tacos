CREATE FUNCTION f_competence_getNextID()
RETURNS BIGINT
AS
BEGIN
DECLARE @highestID bigint, @nextID bigint
SELECT @highestID = max(competence_ID) from competences
if @highestID >0
SET @nextID=@highestID+1
else
SET @nextID = 1
RETURN @nextID
END
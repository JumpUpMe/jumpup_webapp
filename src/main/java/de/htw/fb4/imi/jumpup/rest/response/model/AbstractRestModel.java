package de.htw.fb4.imi.jumpup.rest.response.model;

public class AbstractRestModel
{
    public static final String FIELD_NAME_IDENTITY = "identity";
    public static final String FIELD_NAME_CREATION_TIMESTAMP = "creationTimestamp";
    public static final String FIELD_NAME_UPDATE_TIMESTAMP = "updateTimestamp";

    protected long identity;
    protected long creationTimestamp;
    protected long updateTimestamp;

    public void setIdentity(long identity)
    {
        this.identity = identity;
    }

    public long getIdentity()
    {
        return identity;
    }

    public void setCreationTimestamp(long creationTimestamp)
    {
        this.creationTimestamp = creationTimestamp;
    }

    public long getCreationTimestamp()
    {
        return creationTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp)
    {
        this.updateTimestamp = updateTimestamp;
    }

    public long getUpdateTimestamp()
    {
        return updateTimestamp;
    }
}

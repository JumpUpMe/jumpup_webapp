package de.htw.fb4.imi.jumpup.rest.response.model;

import java.sql.Timestamp;

public class AbstractRestModel
{
    public static final String FIELD_NAME_IDENTITY = "identity";
    public static final String FIELD_NAME_CREATION_TIMESTAMP = "creationTimestamp";
    public static final String FIELD_NAME_UPDATE_TIMESTAMP = "updateTimestamp";

    protected long identity;
    protected Timestamp creationTimestamp;
    protected Timestamp updateTimestamp;

    public void setIdentity(long identity)
    {
        this.identity = identity;
    }

    public long getIdentity()
    {
        return identity;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp)
    {
        this.creationTimestamp = creationTimestamp;
    }

    public Timestamp getCreationTimestamp()
    {
        return creationTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp)
    {
        this.updateTimestamp = updateTimestamp;
    }

    public Timestamp getUpdateTimestamp()
    {
        return updateTimestamp;
    }
}

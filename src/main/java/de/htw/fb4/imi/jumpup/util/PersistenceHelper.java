/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2016 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.util;

import javax.persistence.Query;

import de.htw.fb4.imi.jumpup.util.model.Pagination;

/**
 * <p>
 * Helper for all persistence related tasks.
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 17.02.2016
 *
 */
public class PersistenceHelper
{
    /**
     * Prepare the given query to get the pagination information from the given
     * pagination model.
     * 
     * @param q
     * @param pagination
     * @return
     */
    public static Query addPaginationToQuery(Query q, Pagination pagination)
    {
        return q.setMaxResults(pagination.getLimit())
                .setFirstResult(pagination.getOffset());
    }
}

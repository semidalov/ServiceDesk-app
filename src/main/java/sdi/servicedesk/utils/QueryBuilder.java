package sdi.servicedesk.utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import sdi.servicedesk.services.MinioService;

import java.util.Map;

@Component
public class QueryBuilder {

    private static final Logger LOGGER = Logger.getLogger(QueryBuilder.class);

    public String buildQuery(StringBuilder queryBuilder, Map<String, Object> searchParams, String orderBy,
                             Map<String,Object> filterParams) {

        Boolean whereUsed = false;

        if (searchParams != null)
            for (var entry : searchParams.entrySet()) {
                whereUsed = buildWhereBySearch(queryBuilder, entry.getKey(), entry.getValue(), whereUsed);
            }

        if (filterParams != null)
            for (var entry : filterParams.entrySet()) {
                whereUsed = buildWhereByFilter(queryBuilder, entry.getKey(), entry.getValue(), whereUsed);
            }

        if (orderBy != null)
            buildOrderBy(queryBuilder, orderBy);

        LOGGER.info(queryBuilder);

        return queryBuilder.toString();
    }

    public Boolean buildWhereBySearch(StringBuilder queryBuilder, String param, Object value, Boolean whereUsed) {

        if (value != null) {
            if (!whereUsed) {
                queryBuilder.append(" WHERE ");
                whereUsed = true;
            } else {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append(param).append(" LIKE '%").append(value).append("%'");
        }

        return whereUsed;
    }

    public Boolean buildWhereByFilter(StringBuilder queryBuilder, String param, Object value, Boolean whereUsed) {

        if (value != null) {
            if (!whereUsed) {
                queryBuilder.append(" WHERE ");
                whereUsed = true;
            } else {
                queryBuilder.append(" AND ");
            }

            if (((String) value).charAt(0) == '!')
                queryBuilder.append(param).append(" != '").append(((String) value).substring(1)).append("'");
            else
                queryBuilder.append(param).append(" = '").append(value).append("'");
        }

        return whereUsed;
    }

    public void buildOrderBy(StringBuilder queryBuilder, String orderBy) {
        queryBuilder.append(" ORDER BY ").append(orderBy);
    }
}


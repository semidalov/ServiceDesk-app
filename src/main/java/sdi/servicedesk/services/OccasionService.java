package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.OccasionDAO;
import sdi.servicedesk.models.Occasion;

import java.util.List;
import java.util.Map;

@Service
public class OccasionService {

    private final OccasionDAO occasionDAO;

    @Autowired
    public OccasionService(OccasionDAO occasionDAO) {
        this.occasionDAO = occasionDAO;
    }

    public List<Occasion> getOccasions(int page, int resultsPerPage, Map<String, Object> searchParams) {
        return occasionDAO.findOccasions(page, resultsPerPage, searchParams);
    }

    public int rowsCount(Map<String, Object> searchParams) {
        return occasionDAO.rowsCount(searchParams);
    }
}

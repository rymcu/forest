package com.rymcu.forest.service;

import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.entity.Sponsor;

/**
 * @author ronger
 */
public interface SponsorService extends Service<Sponsor> {
    /**
     * 赞赏
     *
     * @param sponsor
     * @return
     * @throws ServiceException
     */
    boolean sponsorship(Sponsor sponsor) throws ServiceException;
}

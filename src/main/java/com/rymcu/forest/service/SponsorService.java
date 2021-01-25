package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.entity.Sponsor;

import java.util.Map;

/**
 * @author ronger
 */
public interface SponsorService extends Service<Sponsor> {
    /**
     * 赞赏
     * @param sponsor
     * @return
     * @throws Exception
     */
    Map sponsorship(Sponsor sponsor) throws Exception;
}

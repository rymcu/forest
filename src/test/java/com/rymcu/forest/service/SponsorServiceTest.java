package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.entity.Sponsor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 赞赏test
 */
class SponsorServiceTest extends BaseServiceTest {

    @Autowired
    private SponsorService sponsorService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("赞赏")
    void sponsorship() {
        Sponsor sponsor = new Sponsor();
        assertThrows(NullPointerException.class, () -> sponsorService.sponsorship(sponsor));
        sponsor.setSponsor(1L);
        sponsor.setSponsorshipMoney(BigDecimal.TEN);
        sponsor.setDataType(200);
        sponsor.setDataId(65001L);

        assertThrows(DataIntegrityViolationException.class, () -> sponsorService.sponsorship(sponsor));

        sponsor.setDataType(3);
        sponsorService.sponsorship(sponsor);
    }
}
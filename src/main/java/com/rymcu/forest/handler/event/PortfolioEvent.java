package com.rymcu.forest.handler.event;

import com.rymcu.forest.enumerate.OperateType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created on 2024/12/22 20:37.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.handler.event
 */
@Data
@AllArgsConstructor
public class PortfolioEvent {

    private Long idPortfolio;

    private String portfolioTitle;

    private String portfolioDescription;

    private OperateType operateType;

}

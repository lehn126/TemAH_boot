package com.temah.ahfm.controller.alarm;

import com.temah.ahfm.model.Alarm;
import com.temah.common.auth.VerifyToken;
import com.temah.common.page.PagingResult;
import com.temah.common.web.BaseController;
import com.temah.common.web.BaseService;
import com.temah.common.web.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alarm")
public class AlarmController extends BaseController<Alarm, Integer> {

    @Autowired
    public AlarmController(BaseService<Alarm, Integer> service) {
        super(service);
    }

    @GetMapping("/getAll")
    public RestResult findWith(@RequestParam(name = "sortBy", required = false) String sortBy,
                                    @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                    @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
                                    @RequestParam(name = "pageSize", required = false) Integer pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        setPageWithSort(params, sortBy, sortOrder, pageIndex, pageSize, request);
        PagingResult pr = service.findByCriteria(params);
        return RestResult.success(pr);
    }

    @GetMapping("/getAllWithToken")
    @VerifyToken
    public RestResult findWithToken(@RequestParam(name = "sortBy", required = false) String sortBy,
                                    @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                    @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
                                    @RequestParam(name = "pageSize", required = false) Integer pageSize,
                                    HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        setPageWithSort(params, sortBy, sortOrder, pageIndex, pageSize, request);
        PagingResult pr = service.findByCriteria(params);
        return RestResult.success(pr);
    }
}

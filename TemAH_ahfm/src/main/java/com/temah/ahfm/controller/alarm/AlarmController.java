package com.temah.ahfm.controller.alarm;

import com.temah.ahfm.model.Alarm;
import com.temah.ahfm.service.AlarmService;
import com.temah.common.page.PagingResult;
import com.temah.common.web.BaseController;
import com.temah.common.web.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alarm")
public class AlarmController extends BaseController<Alarm, Integer> {

    private final Logger logger = LoggerFactory.getLogger(AlarmController.class);

    private AlarmService alarmService;

    public AlarmController(AlarmService service) {
        super(service);
        this.alarmService = service;
    }

    @PostMapping("/new")
    public RestResult newAlarm(@RequestBody List<Alarm> alarms, HttpServletRequest request) {
        logger.info("收到新的告警信息: {}", alarms);
        int affectedRow = 0;
        if (alarms != null && !alarms.isEmpty()) {
            affectedRow = service.batchCreate(alarms);
        }
        return RestResult.success(affectedRow);
    }

    @PostMapping("/terminate")
    public RestResult terminateAlarm(@RequestBody String ids, HttpServletRequest request) {
        if (ids == null) {
            throw new IllegalArgumentException("缺少必填值");
        }
        logger.info("告警terminate: {}", ids);
        if (ids.startsWith("\"")) {
            ids = ids.substring(1);
        }
        if (ids.endsWith("\"")) {
            ids = ids.substring(0, ids.length() - 1);
        }
        List<String> idList = Arrays.asList(ids.split(","));
        List<Integer> idListID = idList.stream().map(Integer::valueOf).collect(Collectors.toList());

        service.delete(idListID);
        return RestResult.success();
    }

    @PostMapping("/update")
    public RestResult updateAlarm(@RequestBody Alarm alarm, HttpServletRequest request) throws Exception {
        if (alarm == null || alarm.getId() == null) {
            throw new IllegalArgumentException("缺少必填值");
        }
        logger.info("告警update: {}", alarm.getId());
        service.update(alarm);
        return RestResult.success();
    }

    @PostMapping("/clear")
    public RestResult clearAlarm(@RequestParam(name = "id") Integer id, HttpServletRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("缺少必填值");
        }
        logger.info("告警clear: {}", id);
        return RestResult.success(alarmService.clearAlarm(id));
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
}

package org.fffd.l23o6.service.impl;


import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.StationDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.mapper.StationMapper;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.StationEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.station.StationVO;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.service.TrainService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
// TODO: 2023/5/26
// add interface and implement it
public class TrainServiceImpl implements TrainService {
    // cdy : 6.3
    private final TrainDao trainDao;

    @Override
    public TrainDetailVO getTrain(Long trainId) {
        return TrainMapper.INSTANCE.toTrainDetailVO(trainDao.findById(trainId).get());
    }

    @Override
    public List<TrainVO> listTrains(Long startStationId, Long endStationId, String date) {
        return trainDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(TrainMapper.INSTANCE::toTrainVO).collect(Collectors.toList());
    }

    @Override
    public List<AdminTrainVO> listTrainsAdmin() {
        return trainDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(TrainMapper.INSTANCE::toAdminTrainVO).collect(Collectors.toList());
    }

    @Override
    public void addTrain(String name, Long routeId, String type, String date, List<Date> arrivalTimes, List<Date> departureTimes) {
        TrainEntity entity = trainDao.findByName(name);

        if(entity!=null){
            throw new BizException(BizError.STATIONNAME_EXISTS);
        }

        trainDao.save(TrainEntity.builder().name(name).routeId(routeId).seats(new boolean[1][1]).trainType(TrainEntity.TrainType.fromString(type))
                .date(date).arrivalTimes(arrivalTimes).departureTimes(departureTimes).extraInfos(new ArrayList<>()).build());
    }

}
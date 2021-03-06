/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
package org.restcomm.imscf.el.stack;

import org.restcomm.imscf.el.map.MapMobilityManagementListener;
import org.restcomm.imscf.el.map.call.MAPCall;
import org.restcomm.imscf.el.stack.CallContext.ContextLayer;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stack listener for MAP mobility management events, used for AnyTimeInterrogation only.
 */
public class MAPMobilityListener extends ImscfStackListener implements MapMobilityManagementListener {

    private static final Logger LOG = LoggerFactory.getLogger(MAPMobilityListener.class);

    @Override
    public void onErrorComponent(MAPDialog arg0, Long arg1, MAPErrorMessage arg2) {
        try (MAPCall call = (MAPCall) getCallStore().getCallByLocalTcapTrId(arg0.getLocalDialogId())) {
            if (call == null) {
                LOG.warn("Could not find call for onErrorComponent: {}", arg2);
                return;
            }
            try {
                call.getMapModule().onErrorComponent(arg0, arg1, arg2);
            } catch (RuntimeException ex) {
                LOG.error("Exception on MAP processing", ex);
                throw ex;
            }
        }
    }

    @Override
    public void onInvokeTimeout(MAPDialog arg0, Long arg1) {
        try (ContextLayer cl = CallContext.with(callStore)) {
            try (MAPCall call = (MAPCall) getCallStore().getCallByLocalTcapTrId(arg0.getLocalDialogId())) {
                if (call == null) {
                    LOG.warn("Could not find call for onInvokeTimeout: {}", arg0);
                    return;
                }
                try {
                    call.getMapModule().onInvokeTimeout(arg0, arg1);
                } catch (RuntimeException ex) {
                    LOG.error("Exception on MAP processing", ex);
                    throw ex;
                }
            }
        }
    }

    @Override
    public void onMAPMessage(MAPMessage arg0) {
        try (MAPCall call = (MAPCall) getCallStore().getCallByLocalTcapTrId(arg0.getMAPDialog().getLocalDialogId())) {
            if (call == null) {
                LOG.warn("Could not find call for onMAPMessage: {}", arg0);
                return;
            }
            try {
                call.getMapModule().onMAPMessage(arg0);
            } catch (RuntimeException ex) {
                LOG.error("Exception on MAP processing", ex);
                throw ex;
            }
        }
    }

    @Override
    public void onRejectComponent(MAPDialog arg0, Long arg1, Problem arg2, boolean arg3) {
        try (MAPCall call = (MAPCall) getCallStore().getCallByLocalTcapTrId(arg0.getLocalDialogId())) {
            if (call == null) {
                LOG.warn("Could not find call for onRejectComponent: {}", arg0);
                return;
            }
            try {
                call.getMapModule().onRejectComponent(arg0, arg1, arg2, arg3);
            } catch (RuntimeException ex) {
                LOG.error("Exception on MAP processing", ex);
                throw ex;
            }
        }
    }

    @Override
    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse arg0) {
        try (MAPCall call = (MAPCall) getCallStore().getCallByLocalTcapTrId(arg0.getMAPDialog().getLocalDialogId())) {
            if (call == null) {
                LOG.warn("Could not find call for AnyTimeInterrogationResponse: {}", arg0);
                return;
            }
            try {
                call.getMapModule().onAnyTimeInterrogationResponse(arg0);
            } catch (RuntimeException ex) {
                LOG.error("Exception on MAP processing", ex);
                throw ex;
            }
        }
    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest ind) {
        LOG.debug("onAuthenticationFailureReportRequest {}", ind);
    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse ind) {
        LOG.debug("onAuthenticationFailureReportResponse {}", ind);
    }

    @Override
    public void onResetRequest(ResetRequest ind) {
        LOG.debug("onResetRequest {}", ind);
    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest ind) {
        LOG.debug("onForwardCheckSSIndicationRequest {}", ind);
    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest ind) {
        LOG.debug("onRestoreDataRequest {}", ind);
    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse ind) {
        LOG.debug("onRestoreDataResponse {}", ind);
    }

    @Override
    public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request) {
        LOG.debug("onAnyTimeSubscriptionInterrogationRequest {}", request);
    }

    @Override
    public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response) {
        LOG.debug("onAnyTimeSubscriptionInterrogationResponse {}", response);
    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request) {
        LOG.debug("onProvideSubscriberInfoRequest {}", request);
    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response) {
        LOG.debug("onProvideSubscriberInfoResponse {}", response);
    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
        LOG.debug("onDeleteSubscriberDataRequest {}", request);
    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse request) {
        LOG.debug("onDeleteSubscriberDataResponse {}", request);
    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility ind) {
        LOG.debug("onActivateTraceModeRequest_Mobility {}", ind);
    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility ind) {
        LOG.debug("onActivateTraceModeResponse_Mobility {}", ind);
    }

}

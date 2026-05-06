package com.itcen.emergencyroad.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "access_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "log_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = true)
  private User user;

  @Column(name = "action_type", nullable = false, length = 30)
  private String actionType;

  @Column(name = "target_id", nullable = true, length = 50)
  private String targetId;

  @Column(name = "ip_address", nullable = true, length = 45)
  private String ipAddress;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate(){
    this.createdAt = LocalDateTime.now();
  }

  public static AccessLog create(User user, String actionType, String targetId, String ipAddress){
    AccessLog log = new AccessLog();
    log.user = user;
    log.actionType = actionType;
    log.targetId = targetId;
    log.ipAddress = ipAddress;

    return log;
  }
}

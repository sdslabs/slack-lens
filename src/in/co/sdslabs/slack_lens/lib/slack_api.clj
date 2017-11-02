(ns in.co.sdslabs.slack-lens.lib.slack-api
  (:require [clj-slack
             [channels :as channels]]))

(defn- join-channel
  [{:keys [is_channel is_member id name]} options]
  (when (and is_channel (not is_member))
    (channels/join options name)))

(defn join-all-channels
  [options]
  (let [response (channels/list options {:exclude_archived "1"})]
    (if (:ok response)
      (map #(join-channel % options) (:channels response))
      (throw (ex-info "Failed to fetch channel api" {:operation "channel.list"})))))


<script>
export default {
  //组件的name必须以lemonMessage开头，后面跟上 Message.type
  name: "lemonMessageVoice",
  inheritAttrs: false,
  //如果需要使用父组件的方法，可以使用注入。
  inject: ["IMUI"],
  render() {
    //lemon-message-basic 组件对气泡框、头像、事件等信息进行了公共的处理。
    return (
        <lemon-message-basic
            class="lemon-message-voice"
            props={{ ...this.$attrs }}
            scopedSlots={{
              content: props => {
                //返回HTML结构
                return <span><audio controls="controls" style="mini-width:20px" src={props.content} /></span>
              }
            }}
        />
    );
  }
};

</script>

<style>
.lemon-message.lemon-message-voice {
  user-select: none;
}
.lemon-message.lemon-message-voice .lemon-message__content {
  font-size: 12px;
  cursor: pointer;
  border-radius: 7em;
}
.lemon-message.lemon-message-voice .lemon-message__content::before {
  display: none;
}

</style>
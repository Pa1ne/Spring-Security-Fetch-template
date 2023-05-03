const usersTableId = $('#users-table-rows');
const userFormId = $('#user-profile');
const userAddFormId = $('#user-addform');

$('#nav-users_table-link').click(() => {
    loadUsersTable();
});
$('#nav-user_form-link').click(() => {
    loadAddForm();
});
userAddFormId.find(':submit').click(() => {
    insertUser();
});

function loadUsersTable() {
    $('#nav-users_table-link').addClass('active');
    $('#nav-users_table').addClass('show').addClass('active');
    $('#nav-user_form-link').removeClass('active');
    $('#nav-user_form').removeClass('show').removeClass('active');
    getAllUsers();
}

function initNavigation() {
    $('#admin-area-tab').click(() => {
        $('#admin-area-tab').addClass('active').removeClass('btn-link').addClass('btn-primary').prop('aria-selected', true);
        $('#admin-area').addClass('active');
        $('#user-area-tab').removeClass('active').removeClass('btn-primary').addClass('btn-link').prop('aria-selected', false);
        $('#user-area').removeClass('active');
    });
    $('#user-area-tab').click(() => {
        $('#user-area-tab').addClass('active').removeClass('btn-link').addClass('btn-primary').prop('aria-selected', true);
        $('#user-area').addClass('active');
        $('#admin-area-tab').removeClass('active').removeClass('btn-primary').addClass('btn-link').prop('aria-selected', false);
        $('#admin-area').removeClass('active');
    });
}

function loadAddForm() {
    $('#nav-user_form-link').addClass('active');
    $('#nav-user_form').addClass('show').addClass('active');
    $('#nav-users_table-link').removeClass('active');
    $('#nav-users_table').removeClass('show').removeClass('active');
    loadUserForInsertForm();
}

function getAllUsers() {
    fetch('/crud').then(function (response) {
        if (response.ok) {
            response.json().then(users => {
                usersTableId.empty();
                users.forEach(user => {
                    _appendUserRow(user);
                });
            });
        } else {
            console.error('Response: ' + response.status + 'msg: ' + response.statusText);
        }
    });
}

function _appendUserRow(user) {
    usersTableId
        .append($('<tr class="border-top">').attr('id', 'userRow[' + user.id + ']')
            .append($('<td>').attr('id', 'userData[' + user.id + '][id]').text(user.id))
            .append($('<td>').attr('id', 'userData[' + user.id + '][firstName]').text(user.firstName))
            .append($('<td>').attr('id', 'userData[' + user.id + '][lastName]').text(user.lastName))
            .append($('<td>').attr('id', 'userData[' + user.id + '][username]').text(user.username))
            .append($('<td>').attr('id', 'userData[' + user.id + '][age]').text(user.age))
            .append($('<td>').attr('id', 'userData[' + user.id + '][roles]').text(user.roles.map(role => role.name)))
            .append($('<td>').append($('<button class="btn btn-sm btn-info">')
                .click(() => {
                    loadUserAndShowModalForm(user.id);
                }).text('Edit')))
            .append($('<td>').append($('<button class="btn btn-sm btn-danger">')
                .click(() => {
                    loadUserAndShowModalForm(user.id, false);
                }).text('Delete')))
        );
}

function _setReadonlyAttr(flag = true) {
    userFormId.find('#firstName').prop('readonly', flag);
    userFormId.find('#lastName').prop('readonly', flag);
    userFormId.find('#username').prop('readonly', flag);
    userFormId.find('#age').prop('readonly', flag);
    userFormId.find('#password').prop('readonly', flag);
    userFormId.find('#roles').prop('disabled', flag);
}

function updateUser(id) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json; charset=utf-8');
    let user = {
        'id': parseInt(userFormId.find('#id').val()),
        'firstName': userFormId.find('#firstName').val(),
        'lastName': userFormId.find('#lastName').val(),
        'username': userFormId.find('#username').val(),
        'age': userFormId.find('#age').val(),
        'password': userFormId.find('#password').val(),
        'roles': userFormId.find('#roles').val().map(roleId => parseInt(roleId))
    };
    let request = new Request('/crud', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(user)
    });

    fetch(request)
        .then(function (response) {
            if (response.ok) {
                response.json().then(function () {
                    userFormId.modal('hide');
                    loadUsersTable();
                });
            } else {
                console.error('Response: ' + response.status + 'msg: ' + response.statusText);
                userFormId.modal('hide');
                return false;
            }
        })
        .catch(function (err) {
            console.error('Fetch Error :-S', err);
        });
}

function loadUserAndShowModalForm(id, editMode = true) {
    fetch('/crud/' + id, {method: 'GET'})
        .then(function (response) {
                if (response.ok) {
                    response.json().then(function (user) {
                        userFormId.find('#id').val(id);
                        userFormId.find('#firstName').val(user.firstName);
                        userFormId.find('#lastName').val(user.lastName);
                        userFormId.find('#username').val(user.username);
                        userFormId.find('#age').val(user.age);
                        userFormId.find('#password').val('');
                        if (editMode) {
                            userFormId.find('.modal-title').text('Edit user');
                            userFormId.find('#password-div').show();
                            userFormId.find('.submit').text('Edit').removeClass('btn-danger').addClass('btn-primary')
                                .removeAttr('onClick')
                                .attr('onClick', 'updateUser(' + id + ');');
                            _setReadonlyAttr(false);
                        } else {
                            userFormId.find('.modal-title').text('Delete user');
                            userFormId.find('#password-div').hide();
                            userFormId.find('.submit').text('Delete').removeClass('btn-primary').addClass('btn-danger')
                                .removeAttr('onClick')
                                .attr('onClick', 'deleteUser(' + id + ');');
                            _setReadonlyAttr();
                        }

                        fetch('/roles').then(function (response) {
                            if (response.ok) {
                                userFormId.find('#roles').empty();
                                response.json().then(roleList => {
                                    roleList.forEach(role => {
                                        userFormId.find('#roles')
                                            .append($('<option>')
                                                .val(role.id).text(role.name));
                                    });
                                });
                            } else {
                                console.error('Response: ' + response.status + 'msg: ' + response.statusText);
                            }
                        });
                        userFormId.modal();
                    });
                } else {
                    console.error('Response: ' + response.status + 'msg: ' + response.statusText);
                }
            }
        )
        .catch(function (err) {
            console.error('Fetch Error :-S', err);
        });
}

function loadUserForInsertForm() {
    userAddFormId.find('#newfirstName').val('');
    userAddFormId.find('#newlastName').val('');
    userAddFormId.find('#newusername').val('');
    userAddFormId.find('#newage').val('0');
    userAddFormId.find('#newpassword').val('');

    fetch('/roles').then(function (response) {
        if (response.ok) {
            userAddFormId.find('#newroles').empty();
            response.json().then(roleList => {
                roleList.forEach(role => {
                    userAddFormId.find('#newroles')
                        .append($('<option>').val(role.id).text(role.name));
                });
            });
        } else {
            console.error('Response: ' + response.status + 'msg: ' + response.statusText);
        }
    });
}

function insertUser() {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json; charset=utf-8');
    let user = {
        'firstName': userAddFormId.find('#newfirstName').val(),
        'lastName': userAddFormId.find('#newlastName').val(),
        'username': userAddFormId.find('#newusername').val(),
        'age': userAddFormId.find('#newage').val(),
        'password': userAddFormId.find('#newpassword').val(),
        'roles': userAddFormId.find('#newroles').val().map(roleId => parseInt(roleId))
    };
    let request = new Request('/crud', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(user)
    });

    fetch(request)
        .then(function (response) {
            response.json().then(function () {
                loadUsersTable();
            });
        });
}

function deleteUser(id) {
    fetch('/crud/' + id, {method: 'DELETE'})
        .then(function () {
            userFormId.modal('hide');
            usersTableId.find('#userRow\\[' + id + '\\]').remove();
        });
}

$(document).ready(
    () => {
        getAllUsers();
        initNavigation();
    }
);